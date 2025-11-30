package com.ssafy.curtaincall.theater.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ssafy.curtaincall.theater.dto.Theater;
import com.ssafy.curtaincall.theater.mapper.TheaterMapper;

@Service
public class TheaterServiceImpl implements TheaterService{

	@Autowired
	TheaterMapper mapper;
	
	@Override
	public List<Theater> getlist() {
		return mapper.selectAllTheater();
	}

	@Override
	public Theater getTheater(String name) {
		return mapper.selectTheater(name);
	}
	

}
