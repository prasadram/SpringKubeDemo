### Spring Boot Integration with Kubernets ConfigMap

#### Step 1
Add below dependency
```xml
        <dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-kubernetes-config</artifactId>
			<version>1.0.3.RELEASE</version>
	</dependency>

```
#### Step 2
Add below properties in `bootstrap.yml`
```yaml
spring:
  cloud:
    kubernetes:
      config:
        enabled: true
        sources:
          -  namespace: springkube-ns
             name: spring-kube-map
      reload:
        enabled: true
        mode: event
```
And below properties in `application.yml`
```yaml
spring.application.name: SpringKubeDemo

management:
  endpoint:
    restart.enabled: true
```
#### Step 3
Create bean with `@RefreshScope` or `@ConfigurationProperties`
```java
@Configuration
@RefreshScope
@Data
public class GreetingsConfiguration {

    @Value("${message:Hello Welcome To Static Message}")
    private String message;
}
```
#### Step 5
```shell script
mvn clean
mvn package
docker build -t springkubedemo:1.0 .
```
#### Step 5 Kubernetes Setup
I have used `kind` cluster
```shell script
kind create cluster
kind load docker-image springkubedemo:1.0
kubectl config set-context $(kubectl config current-context) --namespace=springkube-ns
kubectl apply -f namespace.yml          
kubectl apply -f serviceaccount.yml
kubectl apply -f svc.yml           
kubectl apply -f configmap.yml       
kubectl apply -f role-rolebinding.yml
kubectl apply -f pod.yml    

```
#### Step 6
How to access Nodeport service

if it is single node cluster find the node ip by using below command
```shell script
kubectl get nodes -o wide
```
then find nodeport by using below command
```shell script
 kubectl get svc
```
In above command there will be port above 30000 which is nodeport

So you can access your service by using `http://nodeip:nodeport`
#### Step 7 Validation
when you start your pod check logs you should see below logs
```ignorelang
2019-11-30 09:20:35.570  INFO [SpringKubeDemo,,,] 1 --- [           main] .r.EventBasedConfigurationChangeDetector : Added new Kubernetes watch: config-maps-watch
2019-11-30 09:20:35.571  INFO [SpringKubeDemo,,,] 1 --- [           main] .r.EventBasedConfigurationChangeDetector : Kubernetes event-based configuration change detector activated
```
Next change any property from configmap and apply it again in kuberentes then it will trigger <br>
and event to our application which will reload all `@RefreshScoped/ConfigurationProperties` beans <br>
we can verify be using below logs
```ignorelang
2019-11-30 09:28:00.975  INFO [SpringKubeDemo,,,] 1 --- [//10.96.0.1/...] .r.EventBasedConfigurationChangeDetector : Detected change in config maps
2019-11-30 09:28:00.975  INFO [SpringKubeDemo,,,] 1 --- [//10.96.0.1/...] .r.EventBasedConfigurationChangeDetector : Reloading using strategy: REFRESH
2019-11-30 09:28:01.098  INFO [SpringKubeDemo,,,] 1 --- [//10.96.0.1/...] trationDelegate$BeanPostProcessorChecker : Bean 'configurationPropertiesRebinderAutoConfiguration' of type [org.springframework.cloud.autoconfigure.ConfigurationPropertiesRebinderAutoConfiguration$$EnhancerBySpringCGLIB$$8fc49f8b] is not eligible for getting processed by all BeanPostProcessors (for example: not eligible for auto-proxying)
2019-11-30 09:28:01.225  INFO [SpringKubeDemo,,,] 1 --- [//10.96.0.1/...] b.c.PropertySourceBootstrapConfiguration : Located property source: CompositePropertySource {name='composite-configmap', propertySources=[ConfigMapPropertySource@2076425053 {name='configmap.spring-kube-map.springkube-ns', properties={message=Welcome To Updated Spring ConfigMap}}]}
2019-11-30 09:28:01.225  INFO [SpringKubeDemo,,,] 1 --- [//10.96.0.1/...] b.c.PropertySourceBootstrapConfiguration : Located property source: SecretsPropertySource {name='secrets.SpringKubeDemo.springkube-ns'}
2019-11-30 09:28:01.230  INFO [SpringKubeDemo,,,] 1 --- [//10.96.0.1/...] o.s.boot.SpringApplication               : The following profiles are active: kubernetes
2019-11-30 09:28:01.237  INFO [SpringKubeDemo,,,] 1 --- [//10.96.0.1/...] o.s.boot.SpringApplication               : Started application in 0.257 seconds (JVM running for 449.649)
``` 
