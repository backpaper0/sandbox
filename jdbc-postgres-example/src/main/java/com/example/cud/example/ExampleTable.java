package com.example.cud.example;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

public class ExampleTable {

	private Integer id;
	private String exampleCol1;
	private String exampleCol2;
	private Integer exampleCol3;
	private Long exampleCol4;
	private BigDecimal exampleCol5;
	private Boolean exampleCol6;
	private LocalDateTime exampleCol7;
	private LocalDate exampleCol8;
	private LocalTime exampleCol9;
	private UUID exampleCol10;
	private Integer version;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getExampleCol1() {
		return exampleCol1;
	}

	public void setExampleCol1(String exampleCol1) {
		this.exampleCol1 = exampleCol1;
	}

	public String getExampleCol2() {
		return exampleCol2;
	}

	public void setExampleCol2(String exampleCol2) {
		this.exampleCol2 = exampleCol2;
	}

	public Integer getExampleCol3() {
		return exampleCol3;
	}

	public void setExampleCol3(Integer exampleCol3) {
		this.exampleCol3 = exampleCol3;
	}

	public Long getExampleCol4() {
		return exampleCol4;
	}

	public void setExampleCol4(Long exampleCol4) {
		this.exampleCol4 = exampleCol4;
	}

	public BigDecimal getExampleCol5() {
		return exampleCol5;
	}

	public void setExampleCol5(BigDecimal exampleCol5) {
		this.exampleCol5 = exampleCol5;
	}

	public Boolean getExampleCol6() {
		return exampleCol6;
	}

	public void setExampleCol6(Boolean exampleCol6) {
		this.exampleCol6 = exampleCol6;
	}

	public LocalDateTime getExampleCol7() {
		return exampleCol7;
	}

	public void setExampleCol7(LocalDateTime exampleCol7) {
		this.exampleCol7 = exampleCol7;
	}

	public LocalDate getExampleCol8() {
		return exampleCol8;
	}

	public void setExampleCol8(LocalDate exampleCol8) {
		this.exampleCol8 = exampleCol8;
	}

	public LocalTime getExampleCol9() {
		return exampleCol9;
	}

	public void setExampleCol9(LocalTime exampleCol9) {
		this.exampleCol9 = exampleCol9;
	}

	public UUID getExampleCol10() {
		return exampleCol10;
	}

	public void setExampleCol10(UUID exampleCol10) {
		this.exampleCol10 = exampleCol10;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
}
