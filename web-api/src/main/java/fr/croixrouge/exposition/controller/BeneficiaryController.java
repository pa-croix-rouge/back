package fr.croixrouge.exposition.controller;

import fr.croixrouge.domain.model.Beneficiary;
import fr.croixrouge.domain.model.ID;
import fr.croixrouge.exposition.dto.core.BeneficiaryCreationRequest;
import fr.croixrouge.exposition.dto.core.BeneficiaryResponse;
import fr.croixrouge.service.BeneficiaryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/beneficiaries")
public class BeneficiaryController extends CRUDController<ID, Beneficiary, BeneficiaryService, BeneficiaryResponse, BeneficiaryCreationRequest> {

    public BeneficiaryController(BeneficiaryService service) {
        super(service);
    }

    //todo : update beneficiary (faire avec verif du token)
    //todo : getBeneficiaryInfo (pour front)


    @Override
    public BeneficiaryResponse toDTO(Beneficiary model) {
        return new BeneficiaryResponse(
                model.getUser().getUsername(),
                model.getFirstName(),
                model.getLastName(),
                model.getPhoneNumber(),
                model.isValidated(),
                model.getLocalUnitId()
        );
    }
}
