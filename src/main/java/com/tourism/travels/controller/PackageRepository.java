package com.tourism.travels.controller;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface PackageRepository extends JpaRepository<PackageEntity, Integer> {

}
