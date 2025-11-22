package com.saas.multitenantplatform.project;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService svc;

    public ProjectController(ProjectService svc) {
        this.svc = svc;
    }

    @GetMapping
    public List<Project> list() {
        return svc.listForCurrentTenant();
    }

    @PostMapping
    public ResponseEntity<Project> create(@RequestBody Project p) {
        Project created = svc.create(p.getName(), p.getDescription());
        return ResponseEntity.created(URI.create("/api/projects/" + created.getId())).body(created);
    }
}
