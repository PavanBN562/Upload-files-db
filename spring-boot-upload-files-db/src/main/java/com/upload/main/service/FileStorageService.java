package com.upload.main.service;

import java.io.IOException;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.upload.main.model.FileDB;
import com.upload.main.repository.FileDBRepository;

@Service
public class FileStorageService {
	
	@Autowired
	public FileDBRepository fileDBRepository;
	
	public FileDB store(MultipartFile file) throws IOException {
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		FileDB fileDB=new FileDB(fileName,file.getContentType(),file.getBytes());
		return fileDBRepository.save(fileDB);
	}

	 public Stream<FileDB> getAllFiles() {
		    return fileDBRepository.findAll().stream();
	}
	 
	  public FileDB getFile(String id) {
		    return fileDBRepository.findById(id).get();
   }
}
