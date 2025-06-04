package houseknxautomation.infrastructure.configuration

import io.calimero.link.KNXNetworkLinkIP
import io.calimero.link.medium.TPSettings
import io.calimero.process.ProcessCommunicatorImpl
import java.net.InetSocketAddress
import kotlin.text.toInt
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
@Profile("default")
class KnxConfiguration {
    @Bean
    fun knxLink(config: HouseKnxAutomationConfigProperties): KNXNetworkLinkIP {
        val localAddress = InetSocketAddress(0)
        val gatewayAddress =
            InetSocketAddress(config.knxGatewayAddress, config.knxGatewayPort.toInt())

        return KNXNetworkLinkIP.newTunnelingLink(localAddress, gatewayAddress, true, TPSettings())
    }

    @Bean
    fun processCommunicator(knxLink: KNXNetworkLinkIP): ProcessCommunicatorImpl {
        return ProcessCommunicatorImpl(knxLink)
    }
}
