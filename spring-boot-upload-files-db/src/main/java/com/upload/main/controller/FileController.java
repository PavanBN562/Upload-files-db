package com.upload.main.controller;


import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.upload.main.message.ResponseFile;
import com.upload.main.message.ResponseMessage;
import com.upload.main.model.FileDB;
import com.upload.main.service.FileStorageService;


@RestController
public class FileController {
	@Autowired
	public FileStorageService fileStorageService;
	
	@PostMapping("/upload")
	public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("files") MultipartFile file) throws IOException{
		 String message = "";
		 try {
			 fileStorageService.store(file);
			 message = "Uploaded the file successfully: "+ file.getOriginalFilename();
			 return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
		 }
		 catch (Exception e)
		 {	
			 message = "file not uploaded successfully" + file.getOriginalFilename();
			 return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
			 
		 } 
	}
	
	@GetMapping("/files")
	  public ResponseEntity<List<ResponseFile>> getListFiles() {
	    List<ResponseFile> files = fileStorageService.getAllFiles().map(dbFile -> {
	      String fileDownloadUri = ServletUriComponentsBuilder
	          .fromCurrentContextPath()
	          .path("/files/")
	          .path(dbFile.getId())
	          .toUriString();

	      return new ResponseFile(
	          dbFile.getName(),
	          fileDownloadUri,
	          dbFile.getType(),
	          dbFile.getData().length);
	    }).collect(Collectors.toList());

	    return ResponseEntity.status(HttpStatus.OK).body(files);
	  }
	
	@GetMapping("/files/{id}")
	  public ResponseEntity<byte[]> getFile(@PathVariable String id) {
	    FileDB fileDB = fileStorageService.getFile(id);

	    return ResponseEntity.ok()
	        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDB.getName() + "\"")
	        .body(fileDB.getData());
	  }
	}
