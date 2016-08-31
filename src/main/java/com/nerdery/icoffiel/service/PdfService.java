package com.nerdery.icoffiel.service;

import com.lowagie.text.DocumentException;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.w3c.dom.Document;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xhtmlrenderer.resource.XMLResource;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Service for generating pdfs.
 */
@Service
@Log
public class PdfService {

    private final SpringTemplateEngine templateEngine;

    @Autowired
    public PdfService(SpringTemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public void generateProposal() throws IOException, DocumentException {
        Context context = new Context();
        context.setVariable("myString", "Hello World");
        String content = templateEngine.process("proposal", context);

        Document document = XMLResource.load(new ByteArrayInputStream(content.getBytes())).getDocument();

        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocument(document, null);

        renderer.layout();

        String fileNameWithPath = "PDF-XhtmlRendered.pdf";
        FileOutputStream fos = new FileOutputStream(fileNameWithPath);
        renderer.createPDF(fos);
        fos.close();
    }
}
