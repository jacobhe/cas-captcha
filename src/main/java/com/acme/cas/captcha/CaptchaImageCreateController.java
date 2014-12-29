package com.acme.cas.captcha;

import com.octo.captcha.service.image.ImageCaptchaService;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import java.io.ByteArrayOutputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class CaptchaImageCreateController implements Controller,
		InitializingBean {
	private ImageCaptchaService jcaptchaService;

	public CaptchaImageCreateController() {
	}

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		byte captchaChallengeAsJpeg[] = null;
		ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
		String captchaId = request.getSession().getId();
		java.awt.image.BufferedImage challenge = jcaptchaService
				.getImageChallengeForID(captchaId, request.getLocale());
		JPEGImageEncoder jpegEncoder = JPEGCodec
				.createJPEGEncoder(jpegOutputStream);

		jpegEncoder.encode(challenge);
		captchaChallengeAsJpeg = jpegOutputStream.toByteArray();
		response.setHeader("Cache-Control", "no-store");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0L);
		response.setContentType("image/jpeg");
		ServletOutputStream responseOutputStream = response.getOutputStream();
		responseOutputStream.write(captchaChallengeAsJpeg);
		responseOutputStream.flush();
		responseOutputStream.close();
		return null;
	}

	public void setJcaptchaService(ImageCaptchaService jcaptchaService) {
		this.jcaptchaService = jcaptchaService;
	}

	public void afterPropertiesSet() throws Exception {
		if (jcaptchaService == null)
			throw new RuntimeException("Image captcha service wasn`t set!");
		else
			return;
	}
}