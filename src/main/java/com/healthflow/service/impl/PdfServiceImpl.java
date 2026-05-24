package com.healthflow.service.impl;

import com.healthflow.common.ResourceNotFoundException;
import com.healthflow.repository.ConsultationRepository;
import com.healthflow.repository.PrescriptionRepository;
import com.healthflow.repository.entity.ConsultationEntity;
import com.healthflow.repository.entity.PrescriptionEntity;
import com.healthflow.service.PdfService;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PdfServiceImpl implements PdfService {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final ConsultationRepository consultationRepository;
    private final PrescriptionRepository prescriptionRepository;

    @Override
    @Transactional(readOnly = true)
    public byte[] generateConsultationPdf(UUID consultationId) {
        ConsultationEntity consultation = consultationRepository.findById(consultationId)
                .orElseThrow(() -> new ResourceNotFoundException("Consultation with id " + consultationId + " not found"));

        List<PrescriptionEntity> prescriptions = prescriptionRepository.findByConsultation_Id(consultationId);

        String html = buildHtml(consultation, prescriptions);
        return renderPdf(html);
    }

    private String buildHtml(ConsultationEntity consultation, List<PrescriptionEntity> prescriptions) {
        String patientFullName = consultation.getAppointment().getPatient().getFirstName() + " "
                + consultation.getAppointment().getPatient().getLastName();
        String doctorFullName = consultation.getAppointment().getDoctor().getFirstName() + " "
                + consultation.getAppointment().getDoctor().getLastName();
        String specialtyName = consultation.getAppointment().getDoctor().getSpecialty().getName();

        StringBuilder prescriptionsRows = new StringBuilder();
        for (PrescriptionEntity prescription : prescriptions) {
            prescriptionsRows.append("<tr>")
                    .append("<td>").append(escapeHtml(prescription.getMedicineName())).append("</td>")
                    .append("<td>").append(escapeHtml(prescription.getDosage())).append("</td>")
                    .append("<td>").append(escapeHtml(prescription.getInstructions())).append("</td>")
                    .append("<td>").append(prescription.getDurationDays()).append("</td>")
                    .append("</tr>");
        }

        if (prescriptionsRows.isEmpty()) {
            prescriptionsRows.append("<tr><td colspan='4'>No prescriptions</td></tr>");
        }

        return """
                <html>
                <head>
                    <style>
                        body { font-family: Arial, sans-serif; margin: 24px; color: #111827; }
                        h1 { font-size: 24px; margin-bottom: 8px; }
                        h2 { font-size: 18px; margin-top: 24px; margin-bottom: 8px; }
                        p { margin: 4px 0; }
                        .meta { margin-bottom: 16px; padding: 12px; background: #f3f4f6; border-radius: 6px; }
                        table { width: 100%%; border-collapse: collapse; margin-top: 8px; }
                        th, td { border: 1px solid #d1d5db; padding: 8px; font-size: 12px; vertical-align: top; }
                        th { background: #f9fafb; text-align: left; }
                        .block { border: 1px solid #e5e7eb; padding: 12px; border-radius: 6px; background: #ffffff; }
                    </style>
                </head>
                <body>
                    <h1>HealthFlow Consultation Report</h1>
                    <div class='meta'>
                        <p><strong>Consultation ID:</strong> %s</p>
                        <p><strong>Date:</strong> %s</p>
                        <p><strong>Patient:</strong> %s</p>
                        <p><strong>Doctor:</strong> %s</p>
                        <p><strong>Specialty:</strong> %s</p>
                    </div>
                                
                    <h2>Clinical Notes</h2>
                    <div class='block'>
                        <p><strong>Symptoms:</strong> %s</p>
                        <p><strong>Diagnosis:</strong> %s</p>
                        <p><strong>Recommendations:</strong> %s</p>
                    </div>
                                
                    <h2>Prescriptions</h2>
                    <table>
                        <thead>
                            <tr>
                                <th>Medicine</th>
                                <th>Dosage</th>
                                <th>Instructions</th>
                                <th>Duration (days)</th>
                            </tr>
                        </thead>
                        <tbody>
                            %s
                        </tbody>
                    </table>
                </body>
                </html>
                """.formatted(
                consultation.getId(),
                consultation.getConsultationDateTime().format(DATE_TIME_FORMATTER),
                escapeHtml(patientFullName),
                escapeHtml(doctorFullName),
                escapeHtml(specialtyName),
                escapeHtml(consultation.getSymptoms()),
                escapeHtml(consultation.getDiagnosis()),
                escapeHtml(consultation.getRecommendations()),
                prescriptionsRows
        );
    }

    private byte[] renderPdf(String html) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withHtmlContent(html, null);
            builder.toStream(outputStream);
            builder.run();
            return outputStream.toByteArray();
        } catch (Exception exception) {
            throw new IllegalStateException("Failed to generate PDF", exception);
        }
    }

    private String escapeHtml(String value) {
        if (value == null) {
            return "";
        }

        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
