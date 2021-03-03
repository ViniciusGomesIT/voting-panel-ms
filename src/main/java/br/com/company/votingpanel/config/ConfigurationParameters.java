package br.com.company.votingpanel.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConfigurationParameters {

	//MESSAGE SOURCE PROPERTIES
	@Value("{config.messageSource.defaultEncoding}")
	private String defaultEncoding;
	
	@Value("{config.messageSource.defaultLocale}")
	private String defaultLocale;
	
	@Value("{config.messageSource.defaultCacheLifeTime}")
	private Integer defaultCacheLifeTime;
	
	@Value("{config.messageSource.defaultBaseName}")
	private String defaultBaseName;
	
	//VALIDATION PROPERTIES
	@Value("{config.validation.numericRegexValidator}")
	private String numericRegexValidator;
	
	@Value("{config.validation.defaultCpfLengthValidator}")
	private Integer defaultCpfLengthValidator;
	
	@Value("{config.validation.defaultSessionExpirationTimeInMinutes}")
	private Long defaultSessionExpirationTimeInMinutes;

	public String getDefaultEncoding() {
		return defaultEncoding;
	}

	public String getDefaultLocale() {
		return defaultLocale;
	}

	public Integer getDefaultCacheLifeTime() {
		return defaultCacheLifeTime;
	}

	public String getDefaultBaseName() {
		return defaultBaseName;
	}

	public String getNumericRegexValidator() {
		return numericRegexValidator;
	}

	public Integer getDefaultCpfLengthValidator() {
		return defaultCpfLengthValidator;
	}

	public Long getDefaultSessionExpirationTimeInMinutes() {
		return defaultSessionExpirationTimeInMinutes;
	}
}
