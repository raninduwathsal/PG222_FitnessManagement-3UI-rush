package com.fitness.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.io.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.json.JSONObject;
import org.json.JSONArray;

@Controller
@RequestMapping("/api/reviews")
public class ReviewController {
    private static final String REVIEWS_FILE = "reviews.txt";

    @PostMapping("/submit")
    @ResponseBody
    public Map<String, String> submitReview(@RequestParam String name, @RequestParam String review) {
        try {
            JSONObject reviewObj = new JSONObject();
            reviewObj.put("name", name);
            reviewObj.put("review", review);
            reviewObj.put("date", LocalDateTime.now().toString());

            FileWriter fw = new FileWriter(REVIEWS_FILE, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(reviewObj.toString());
            bw.newLine();
            bw.close();

            return Collections.singletonMap("status", "success");
        } catch (IOException e) {
            return Collections.singletonMap("status", "error");
        }
    }

    @GetMapping("/all")
    @ResponseBody
    public String getAllReviews() {
        JSONArray reviews = new JSONArray();
        try {
            File file = new File(REVIEWS_FILE);
            if (!file.exists()) {
                return "[]";
            }

            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                reviews.put(new JSONObject(line));
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
            return "[]";
        }
        return reviews.toString();
    }
}