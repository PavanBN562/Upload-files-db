package com.upload.main.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.upload.main.model.FileDB;

public interface FileDBRepository extends JpaRepository<FileDB, String>  {
	
}
