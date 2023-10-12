package com.saptak.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "optjbr00")
@Data
public class JobRequest {

	@Id
	@GeneratedValue
	private int jobId;

	private String statusCode;
	private String emailAddres;
	private LocalDateTime jbrInsertTms;
	private LocalDateTime jbrUpdateTms;
	private String jbrInsertOperId;
	private String jbrUpdateOperId;
	private String category;

}
