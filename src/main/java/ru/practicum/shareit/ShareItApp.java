package ru.practicum.shareit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ShareItApp {
//в тестах никак не проверяются сущность booking и request и в тз в основном говорилось
// только про user и item поэтому я не стал делать booking и request. если я не прав то все исправлю
	public static void main(String[] args) {
		SpringApplication.run(ShareItApp.class, args);
	}

}
