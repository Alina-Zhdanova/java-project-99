package hexlet.code.service;

import hexlet.code.dto.LabelCreateDTO;
import hexlet.code.dto.LabelDTO;
import hexlet.code.dto.LabelUpdateDTO;
import hexlet.code.exception.LabelHasTasksException;
import hexlet.code.exception.ResourceNotFoundException;
import hexlet.code.mapper.LabelMapper;
import hexlet.code.repository.LabelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LabelServise {
    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private LabelMapper labelMapper;

    public List<LabelDTO> getAllLabel() {
        var labels = labelRepository.findAll();
        var result = labels.stream()
            .map(labelMapper::map)
            .toList();

        return result;
    }

    public LabelDTO createLabel(LabelCreateDTO labelCreateDTO) {
        var label = labelMapper.map(labelCreateDTO);
        labelRepository.save(label);
        var labelDTO = labelMapper.map(label);

        return labelDTO;
    }

    public LabelDTO findLabelById(Long id) {
        var label = labelRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Label with id " + id + "not found"));
        var labelDTO = labelMapper.map(label);

        return labelDTO;
    }

    public LabelDTO updateLabel(LabelUpdateDTO labelUpdateDTO, Long id) {
        var label = labelRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Label with id " + id + "not found"));
        labelMapper.update(labelUpdateDTO, label);
        labelRepository.save(label);
        var labelDTO = labelMapper.map(label);

        return labelDTO;
    }

    public void deleteLabel(Long id) {
        try {
            labelRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new LabelHasTasksException("Deletion is not possible, the User is associated with one or more tasks");
        }
    }
}
