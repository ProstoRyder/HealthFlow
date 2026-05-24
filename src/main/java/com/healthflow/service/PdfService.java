package com.healthflow.service;

import java.util.UUID;

public interface PdfService {

    byte[] generateConsultationPdf(UUID consultationId);
}
