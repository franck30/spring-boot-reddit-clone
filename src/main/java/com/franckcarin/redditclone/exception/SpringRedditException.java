package com.franckcarin.redditclone.exception;

import org.springframework.mail.MailException;

public class SpringRedditException extends RuntimeException {
  public SpringRedditException(String exMessage) {
    super(exMessage);
  }
}
