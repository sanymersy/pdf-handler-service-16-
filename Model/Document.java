package com.document.Model;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@EnableJpaRepositories
public class Document{


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;


	private String Name;

	private String description;

	private String pdf_name;

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "pdfdata", length = 100000)
	private byte[] pdf;

	public Document(Long id, String name, String description, String pdf_name, byte[] pdf) {
		this.id = id;
		Name = name;
		this.description = description;
		this.pdf_name = pdf_name;
		this.pdf = pdf;
	}

	public Document() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPdf_name() {
		return pdf_name;
	}

	public void setPdf_name(String pdf_name) {
		this.pdf_name = pdf_name;
	}

	public byte[] getPdf() {
		return pdf;
	}

	public void setPdf(byte[] pdf) {
		this.pdf = pdf;
	}
}