package br.com.company.votingpanel.config;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Value;
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

	@Value("${config.messageSource.defaultEncoding}")
	private String defaultEncoding;

	@Value("${config.messageSource.defaultLocale}")
	private String defaultLocale;

	@Value("${config.messageSource.defaultBaseName}")
	private String defaultBaseName;

	@Value("${config.messageSource.defaultCacheLifeTime}")
	private Integer cacheLifeTime;

	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasenames("classpath:" + defaultBaseName);
		messageSource.setDefaultEncoding(defaultEncoding);
		messageSource.setCacheSeconds(cacheLifeTime);

		return messageSource;
	}

	@Bean
	public LocaleResolver localeResolver() {
		SessionLocaleResolver localResolver = new SessionLocaleResolver();
		localResolver.setDefaultLocale(Locale.forLanguageTag(defaultLocale));

		return localResolver;
	}
}
