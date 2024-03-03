package ma.enset.hospital;

import ma.enset.hospital.entities.*;
import ma.enset.hospital.repositories.ConsultationRepository;
import ma.enset.hospital.repositories.MedecinRepository;
import ma.enset.hospital.repositories.PatientRepository;
import ma.enset.hospital.repositories.RendezVousRepository;
import ma.enset.hospital.service.IHospitalService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.stream.Stream;

@SpringBootApplication
public class HospitalApplication  {

	public static void main(String[] args) {
		SpringApplication.run(HospitalApplication.class, args);
	}

	@Bean
	CommandLineRunner start(IHospitalService hospitalService,PatientRepository patientRepository,RendezVousRepository rendezVousRepository,ConsultationRepository consultationRepository,MedecinRepository medecinRepository){

		return args -> {

			Stream.of("Aymane","Hanan","Yasmine").forEach(name->{
				Medecin medecin=new Medecin();
				medecin.setNom(name);
				medecin.setSpecialite(Math.random()>0.5?"Cardio":"Dentiste");
				medecin.setEmail(name+"@gmail.com");
				hospitalService.saveMedecin(medecin);
			});
			Stream.of("Mohammed","Hassan","Najat").forEach(name->{
				Patient patient=new Patient();
				patient.setNom(name);
				patient.setMalade(false);
				patient.setDateNaissance(new Date());
				hospitalService.savePatient(patient);
			});

			Patient patient=patientRepository.findByNom("Hassan");
			Medecin medecin=medecinRepository.findByNom("Yasmine");
			RendezVous rendezVous=new RendezVous();
			rendezVous.setDateRDV(new Date());
			rendezVous.setStatus(StatusRdv.PENDING);
			rendezVous.setMedecin(medecin);
			rendezVous.setPatient(patient);

			RendezVous saveRdv=hospitalService.saveRendezVous(rendezVous);
			System.out.println(saveRdv.getId());
			rendezVous= rendezVousRepository.findAll().get(0);
			Consultation consultation=new Consultation();
			consultation.setDateConsultation(rendezVous.getDateRDV());
			consultation.setRapport("Rapport consultation");
			consultation.setRendezVous(rendezVous);
			hospitalService.saveConsultation(consultation);






		};
	}
}
