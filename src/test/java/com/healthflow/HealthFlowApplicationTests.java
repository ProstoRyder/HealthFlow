package com.healthflow;

import com.healthflow.repository.AppointmentRepository;
import com.healthflow.repository.ConsultationRepository;
import com.healthflow.repository.DoctorRepository;
import com.healthflow.repository.DoctorScheduleRepository;
import com.healthflow.repository.HospitalRepository;
import com.healthflow.repository.PatientRepository;
import com.healthflow.repository.PrescriptionRepository;
import com.healthflow.repository.ReviewRepository;
import com.healthflow.repository.SpecialtyRepository;
import com.healthflow.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(properties = {
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration," +
                "org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration"
})
class HealthFlowApplicationTests {

    @MockBean
    private SpecialtyRepository specialtyRepository;

    @MockBean
    private HospitalRepository hospitalRepository;

    @MockBean
    private DoctorRepository doctorRepository;

    @MockBean
    private DoctorScheduleRepository doctorScheduleRepository;

    @MockBean
    private PatientRepository patientRepository;

    @MockBean
    private AppointmentRepository appointmentRepository;

    @MockBean
    private ConsultationRepository consultationRepository;

    @MockBean
    private PrescriptionRepository prescriptionRepository;

    @MockBean
    private ReviewRepository reviewRepository;

    @MockBean
    private UserRepository userRepository;

    @Test
    void contextLoads() {
    }
}
