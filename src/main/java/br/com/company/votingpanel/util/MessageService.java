package br.com.company.votingpanel.util;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

	private MessageSource messageSource;
	
	@Autowired
	public MessageService(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public String getMessage(String key)  {
		return getMessage(key, "");
	}
	
	public String getMessage(String key, String... args) {
		return messageSource.getMessage(key, args, Locale.getDefault());
	}
}
