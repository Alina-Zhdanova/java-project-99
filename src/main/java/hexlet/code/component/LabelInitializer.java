package hexlet.code.component;

import hexlet.code.model.Label;
import hexlet.code.repository.LabelRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class LabelInitializer implements ApplicationRunner {
    @Autowired
    private LabelRepository labelRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        var bugLabel = new Label();
        bugLabel.setName("bug");
        var featureLabel = new Label();
        featureLabel.setName("feature");

        labelRepository.save(bugLabel);
        labelRepository.save(featureLabel);
    }
}

