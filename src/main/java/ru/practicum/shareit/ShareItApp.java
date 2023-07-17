package ru.practicum.shareit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ShareItApp {
	public static void main(String[] args) {
		SpringApplication.run(ShareItApp.class, args);
	}
//большая проблема с тестами. я написал много лишних тестов потому что не пропускала проверка на покрытие кода тестами
	// и сами тесты мне кажутся неправильными
}
