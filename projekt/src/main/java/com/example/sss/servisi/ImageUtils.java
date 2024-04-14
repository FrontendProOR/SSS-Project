package com.example.sss.servisi;

import com.example.sss.model.Agent;
import com.example.sss.model.DTO.NekretninaDTO;
import com.example.sss.model.ImagePath;
import com.example.sss.repozitorijumi.SlikaRepozitorijum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

@Component
public class ImageUtils {

    @Autowired
    SlikaServis slikaServis;

    public List<String> getSlikeUBase64(Integer id) {
        List<ImagePath> slikeUBase64 = slikaServis.getSlikeNekretnine(id);
        List<String> putanje = slikeUBase64.stream()
                .map(ImagePath::getImagePath)
                .collect(Collectors.toList());
        for(String slika : putanje){
            System.out.println(slika);
        }

        List<String> enkodiraneSlike = new ArrayList<>();
        for(String slika : putanje){
            File putanja = new File(slika);
            try {
                byte[] imageData = Files.readAllBytes(putanja.toPath());
                String base64Slika = Base64.getEncoder().encodeToString(imageData);
                enkodiraneSlike.add(base64Slika);
            }
            catch (IOException e) {
                e.printStackTrace();
                break;
            }

        }
        return enkodiraneSlike;
    }
}
