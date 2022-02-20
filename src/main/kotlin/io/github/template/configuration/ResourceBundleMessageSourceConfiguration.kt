package io.github.template.configuration

import dev.akkinoc.util.YamlResourceBundle
import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.MessageSourceAccessor
import org.springframework.context.support.ResourceBundleMessageSource
import java.util.*

@Configuration
class ResourceBundleMessageSourceConfiguration {
    @Bean
    fun messageSource(): ResourceBundleMessageSource {
        val rs = YamlMessageSource()
        rs.setBasename("i18n/messages")
        rs.setDefaultEncoding("UTF-8")
        rs.setUseCodeAsDefaultMessage(true)
        rs.setCacheSeconds(60)
        return rs
    }

    @Bean
    fun messageSourceAccessor(messageSource: MessageSource): MessageSourceAccessor {
        return MessageSourceAccessor(messageSource)
    }

    private class YamlMessageSource : ResourceBundleMessageSource() {
        @Throws(MissingResourceException::class)
        override fun doGetBundle(basename: String, locale: Locale): ResourceBundle {
            return ResourceBundle.getBundle(basename, locale, YamlResourceBundle.Control)
        }
    }
}
