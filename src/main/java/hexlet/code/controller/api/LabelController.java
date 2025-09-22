package hexlet.code.controller.api;

import hexlet.code.dto.LabelCreateDTO;
import hexlet.code.dto.LabelDTO;
import hexlet.code.dto.LabelUpdateDTO;
import hexlet.code.service.LabelServise;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/labels")
public class LabelController {
    @Autowired
    private LabelServise labelServise;

    @GetMapping("")
    public List<LabelDTO> index() {
        return labelServise.getAllLabel();
    }

    @GetMapping("/{id}")
    public LabelDTO show(@PathVariable Long id) {
        return labelServise.findLabelById(id);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public LabelDTO create(@Valid @RequestBody LabelCreateDTO labelCreateDTO) {
        return labelServise.createLabel(labelCreateDTO);
    }

    @PutMapping("/{id}")
    public LabelDTO update(@Valid @RequestBody LabelUpdateDTO labelUpdateDTO, @PathVariable Long id) {
        return labelServise.updateLabel(labelUpdateDTO, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void destroy(@PathVariable Long id) {
        labelServise.deleteLabel(id);
    }
}
