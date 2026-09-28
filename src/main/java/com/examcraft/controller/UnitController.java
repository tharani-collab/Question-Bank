package com.examcraft.controller;

import com.examcraft.entity.Unit;
import com.examcraft.service.UnitService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/units")
public class UnitController {

    private final UnitService service;

    public UnitController(UnitService service) {
        this.service = service;
    }

    @GetMapping
    public List<Unit> getAll() {
        return service.getAllUnits();
    }

    @GetMapping("/{id}")
    public Unit getById(@PathVariable Long id) {
        return service.getUnitById(id);
    }

    @PostMapping
    public Unit add(@RequestBody Unit unit) {
        return service.addUnit(unit);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        service.deleteUnit(id);
        return "Unit deleted successfully";
    }
}