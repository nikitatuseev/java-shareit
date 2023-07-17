package ru.practicum.shareit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ShareItApp {
	public static void main(String[] args) {
		SpringApplication.run(ShareItApp.class, args);
	}
//большая проблема с тестами. я написал много лишних тестов потому что  проверка на покрытие кода тестами не проходит уже
	//который раз и я не опнимаю как это исправить
	// и сами тесты мне кажутся неправильными
}
