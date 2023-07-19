package ru.practicum.shareit.itemRequest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.request.dto.NewRequestDto;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class NewRequestDtoTests {
    @Autowired
    private JacksonTester<NewRequestDto> json;

    @Test
    void testCreationRequestDto() throws Exception {
        NewRequestDto creationRequestDto = new NewRequestDto(
                "text"
        );

        JsonContent<NewRequestDto> result = json.write(creationRequestDto);

        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("text");
    }
}