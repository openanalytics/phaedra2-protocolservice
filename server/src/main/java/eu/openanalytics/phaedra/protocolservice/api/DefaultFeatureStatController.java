package eu.openanalytics.phaedra.protocolservice.api;

import eu.openanalytics.phaedra.protocolservice.dto.DefaultFeatureStatDTO;
import eu.openanalytics.phaedra.protocolservice.dto.validation.OnCreate;
import eu.openanalytics.phaedra.protocolservice.dto.validation.OnUpdate;
import eu.openanalytics.phaedra.protocolservice.exception.UserVisibleException;
import eu.openanalytics.phaedra.protocolservice.service.DefaultFeatureStatService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
@Validated
public class DefaultFeatureStatController extends BaseController {

    private final DefaultFeatureStatService defaultFeatureStatService;

    public DefaultFeatureStatController(DefaultFeatureStatService defaultFeatureStatService) {
        this.defaultFeatureStatService = defaultFeatureStatService;
    }


    @PostMapping("/defaultfeaturestat")
    @ResponseStatus(HttpStatus.CREATED)
    public DefaultFeatureStatDTO createDefaultFeatureStat(@Validated(OnCreate.class) @RequestBody DefaultFeatureStatDTO defaultFeatureStatDTO) throws UserVisibleException {
        return defaultFeatureStatService.create(defaultFeatureStatDTO);
    }

    /**
     * Performs an update of the DefaultFeatureStat, replacing the DefaultFeatureStat (not patching it!).
     * This corresponds to the idea of RESTFUL: https://stackoverflow.com/a/19736570/1393103
     */
    @PutMapping("/defaultfeaturestat/{defaultFeatureStatId}")
    @ResponseStatus(HttpStatus.OK)
    public DefaultFeatureStatDTO updateDefaultFeatureStat(@PathVariable Long defaultFeatureStatId,
                                                          @Validated(OnUpdate.class) @RequestBody DefaultFeatureStatDTO defaultFeatureStatDTO) throws UserVisibleException {

        if (!Objects.equals(defaultFeatureStatDTO.getId(), defaultFeatureStatId)) {
            throw new UserVisibleException("The defaultFeatureStatDTO provided in the URL is not equal to the id provided in the body");
        }

        return defaultFeatureStatService.update(defaultFeatureStatDTO);
    }

    @DeleteMapping("/defaultfeaturestat/{defaultFeatureStatId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDefaultFeatureStat(@PathVariable Long defaultFeatureStatId) throws UserVisibleException {
        defaultFeatureStatService.delete(defaultFeatureStatId);
    }

    @GetMapping("/defaultfeaturestat/{defaultFeatureStatId}")
    @ResponseStatus(HttpStatus.OK)
    public DefaultFeatureStatDTO getDefaultFeatureStat(@PathVariable Long defaultFeatureStatId) throws UserVisibleException {
        return defaultFeatureStatService.get(defaultFeatureStatId);
    }

    @GetMapping("/defaultfeaturestat")
    @ResponseStatus(HttpStatus.OK)
    public List<DefaultFeatureStatDTO> getDefaultFeatureStats() {
        return defaultFeatureStatService.get();
    }

}
