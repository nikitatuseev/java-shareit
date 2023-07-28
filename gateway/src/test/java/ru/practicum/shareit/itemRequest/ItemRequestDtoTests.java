package ru.practicum.shareit.itemRequest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class ItemRequestDtoTests {
    @Autowired
    private JacksonTester<ItemRequestDto> json;

    @Test
    void testBookingDto() throws Exception {
        ItemRequestDto requestDto = new ItemRequestDto(
                1,
                "text",
                LocalDateTime.of(2022,2,3,4,5),
                1
        );
        JsonContent<ItemRequestDto> result = json.write(requestDto);

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo("text");
        assertThat(result).extractingJsonPathNumberValue("$.requestorId").isEqualTo(1);
    }
}