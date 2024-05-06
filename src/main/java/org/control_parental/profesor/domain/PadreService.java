package org.control_parental.profesor.domain;

import org.control_parental.profesor.infrastructure.PadreRepository;
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
