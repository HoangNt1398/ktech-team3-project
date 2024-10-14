package com.example.TeamPj.tags;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagsService {
    @Autowired
    private TagsRepository tagsRepository;
    public List<Tags> getAllTags() {
        return tagsRepository.findAll();
    }
    public Tags getTagById(Long id) {
        return(Tags) tagsRepository.findById(id).orElseThrow(()
        -> new RuntimeException("tag is not found"));
    }
    public Tags addTag(Tags tag) {
        return tagsRepository.save(tag);
    }
    public Tags deleteTag(Long id) {
        tagsRepository.deleteById(id);

        return null;
    }
}
