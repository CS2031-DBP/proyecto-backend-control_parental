package org.control_parental.nido.Application;

import org.control_parental.nido.Domain.NidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/nido")

public class NidoController {

    @Autowired
    NidoService nidoService;

    @GetMapping
    ResponseEntity<String> getName() {
        return ResponseEntity.ok(nidoService.getName());
    }


}
