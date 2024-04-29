package org.control_parental.padre.domain;

import org.control_parental.padre.infrastructure.PadreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PadreService {
    @Autowired
    PadreRepository repository;

    public void savePadre(Padre padre) {
        repository.save(padre);
    }

    public Padre getPadreById(Long id) {
        return repository.findById(id).orElseThrow();
    }
}
