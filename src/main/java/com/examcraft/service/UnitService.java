package com.examcraft.service;

import com.examcraft.entity.Unit;
import com.examcraft.repository.UnitRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UnitService {

    private final UnitRepository repository;

    public UnitService(UnitRepository repository) {
        this.repository = repository;
    }

    public List<Unit> getAllUnits() {
        return repository.findAll();
    }

    public Unit getUnitById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Unit not found"));
    }

    public Unit addUnit(Unit unit) {
        return repository.save(unit);
    }

    public void deleteUnit(Long id) {
        repository.deleteById(id);
    }
}