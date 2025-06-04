package houseknxautomation.infrastructure.configuration

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.EnableAspectJAutoProxy

@Configuration
@EnableAspectJAutoProxy
@ComponentScan(basePackages = ["houseknxautomation.infrastructure.aspects"])
class AspectConfiguration {}
