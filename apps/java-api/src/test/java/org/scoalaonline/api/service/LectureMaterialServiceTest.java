package org.scoalaonline.api.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.scoalaonline.api.repository.LectureMaterialRepository;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class LectureMaterialServiceTest {

  @Mock
  private LectureMaterialRepository lectureMaterialRepository;
  private LectureMaterialService underTest;

  @BeforeEach
  void setUp() {
    underTest = new LectureMaterialService(lectureMaterialRepository);
  }


  @Test
  @Disabled
  void getAll() {
  }

  @Test
  @Disabled
  void getOneById() {
  }

  @Test
  @Disabled
  void add() {
  }

  @Test
  @Disabled
  void update() {
  }

  @Test
  @Disabled
  void delete() {
  }
}
