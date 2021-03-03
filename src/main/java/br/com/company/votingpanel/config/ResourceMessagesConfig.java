package br.com.company.votingpanel.config;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

@Component
@Configuration
public class ResourceMessagesConfig {

	private final ConfigurationParameters configurationParameters;
	
	public ResourceMessagesConfig(ConfigurationParameters configurationParameters) {
		this.configurationParameters = configurationParameters;
	}

	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasenames(configurationParameters.getDefaultBaseName());
		messageSource.setDefaultEncoding(configurationParameters.getDefaultEncoding());
		messageSource.setCacheSeconds(configurationParameters.getDefaultCacheLifeTime());

		return messageSource;
	}

	@Bean
	public LocaleResolver localeResolver() {
		SessionLocaleResolver localResolver = new SessionLocaleResolver();
		localResolver.setDefaultLocale(Locale.forLanguageTag(configurationParameters.getDefaultLocale()));

		return localResolver;
	}
}
