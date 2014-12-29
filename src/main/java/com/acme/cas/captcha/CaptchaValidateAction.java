package com.acme.cas.captcha;

import org.jasig.cas.web.support.WebUtils;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.webflow.execution.RequestContext;

import com.octo.captcha.service.CaptchaServiceException;
import com.octo.captcha.service.image.ImageCaptchaService;

public class CaptchaValidateAction {
	private ImageCaptchaService captchaService;
	private String captchaValidationParameter = "j_captcha_response";
	public static final String CODE = "error.authentication.credentials.captcha";

	public void setCaptchaService(ImageCaptchaService captchaService) {
		this.captchaService = captchaService;
	}

	public void setCaptchaValidationParameter(String captchaValidationParameter) {
		this.captchaValidationParameter = captchaValidationParameter;
	}
	public final String submit(final RequestContext context, final MessageContext messageContext) throws Exception {
		
		String captcha_response = context.getRequestParameters().get(
				captchaValidationParameter);
		boolean valid = false;

		if (captcha_response != null) {
			String id = WebUtils.getHttpServletRequest(context).getSession()
					.getId();
			if (id != null) {
				try {
					valid = captchaService.validateResponseForID(id,
							captcha_response).booleanValue();
				} catch (CaptchaServiceException cse) {
				}
			}
		}

		if (valid) {
			return "success";
		}
		messageContext.addMessage(new MessageBuilder().error().code(CODE)
				.defaultText(CODE).build());
		return "error";
    }
}
