package com.project.dechelper.bootData;

import aj.org.objectweb.asm.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.dechelper.model.Information;
import com.project.dechelper.repositories.InformationRepository;
import lombok.Builder;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

@Component
@Builder
@Order(1)
public class DataLoader implements ApplicationRunner {

    private InformationRepository informationRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        informationRepository.saveAll(getInformationsFromJson("/json/sample_records.json"));
    }

    private List<Information> getInformationsFromJson(String json) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        InputStream jsonStream = TypeReference.class.getResourceAsStream(json);
        return Arrays.asList(mapper.readValue(jsonStream, Information[].class));
    }

    private List<Information> createInformations() {
        Information inf1 = Information.builder()
                .subject("Cousin Martin")
                .type("Family")
                .content("Family"+" | "+"Cousin Martin"+" | "+"On 01.02.2023, he borrowed me 123$")
                .build();
        Information inf2 = Information.builder()
                .subject("Aunt Margaret")
                .type("Family")
                .content("Family"+" | "+"Aunt Margaret"+" | "+"She cannot accept change in government")
                .build();
        Information inf3 = Information.builder()
                .subject("Louis Cabbage")
                .type("Work")
                .content("Work"+" | "+"Louis Cabbage"+" | "+"Probably, he tell boss that im not good enough")
                .build();
        Information inf4 = Information.builder()
                .subject("Bethany Kapusta")
                .type("Friends")
                .content("Friends"+" | "+"Bethany Kapusta"+" | "+"She likes when somebody gives her flowers")
                .build();
        Information inf5 = Information.builder()
                .subject("Pablo Eskamoso")
                .type("Work")
                .content("Work"+" | "+"Pablo Eskamoso"+" | "+"He steals paper sheets from work")
                .build();
        Information inf6 = Information.builder()
                .subject("Project Master John")
                .type("Work")
                .content("Work"+" | "+"Project Master John"+" | "+"He is fine with my work, but I can do better")
                .build();
        return Arrays.asList(inf1, inf2, inf3, inf4, inf5, inf6);
    }

}
