package com.sistema.examenes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.hibernate.Hibernate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@SpringBootTest
class SistemaExamenesBackendApplicationTests {
	
	@Test
	void contextLoads() {
		try {
			if(prueba()) {
				System.out.println("true");
			}else {
				System.out.println("false");	
			}
		}catch(Error e) {//catch(Exception e)
			System.out.println(e.getMessage());
		}
	}
	
	boolean prueba() throws Error {
		throw new Error("ESTOY HACIENDO UN TEST :)");
	}

}
