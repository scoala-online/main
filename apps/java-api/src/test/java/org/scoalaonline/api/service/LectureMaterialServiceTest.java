package org.scoalaonline.api.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.scoalaonline.api.exception.LectureMaterialInvalidDocumentException;
import org.scoalaonline.api.exception.LectureMaterialNotFoundException;
import org.scoalaonline.api.model.LectureMaterial;
import org.scoalaonline.api.repository.LectureMaterialRepository;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LectureMaterialServiceTest {

  @Mock
  private LectureMaterialRepository lectureMaterialRepository;
  private LectureMaterialService underTestService;

  @BeforeEach
  void setUp() {
    underTestService = new LectureMaterialService(lectureMaterialRepository);
  }


  @Test
  void getAllTest() {
    // when
    underTestService.getAll();

    // then
    verify(lectureMaterialRepository).findAll();
  }

  @Test
  void getOneByIdTest() throws LectureMaterialNotFoundException {
    // when
    when(lectureMaterialRepository.findById(anyString()))
      .thenReturn(java.util.Optional.of(new LectureMaterial("id", "Document.pdf")));

    // then
    LectureMaterial lectureMaterial = underTestService.getOneById("id");

    Assertions.assertEquals("Document.pdf", lectureMaterial.getDocument());
  }

  @Test
  void getOneByIdExceptionTest() {
    //when
    when(lectureMaterialRepository.findById(anyString()))
      .thenReturn(Optional.empty());

    //then
    assertThatThrownBy(() -> underTestService.getOneById(anyString()))
      .isInstanceOf(LectureMaterialNotFoundException.class)
      .hasMessageContaining("Method getOneById: Lecture Material not found");
  }

  @Test
  void add() throws LectureMaterialInvalidDocumentException {
    // given
    LectureMaterial lectureMaterial = new LectureMaterial("string_id", "Some_Document.pdf");

    // when
    underTestService.add(lectureMaterial);
    
    // then
    ArgumentCaptor<LectureMaterial> lectureMaterialArgumentCaptor =
      ArgumentCaptor.forClass(LectureMaterial.class);

    verify(lectureMaterialRepository).save(lectureMaterialArgumentCaptor.capture());

    LectureMaterial capturedLectureMaterial = lectureMaterialArgumentCaptor.getValue();
    assertThat(capturedLectureMaterial.getDocument()).isEqualTo(lectureMaterial.getDocument());
  }

  //alta varianta

//    @Test
//    public void savedCustomer_Success() {
//        Customer customer = new Customer();
//        customer.setFirstName("sajedul");
//        customer.setLastName("karim");
//        customer.setMobileNumber("01737186095");
//
//        // providing knowledge
//        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
//
//        Customer savedCustomer = customerRepository.save(customer);
//        assertThat(savedCustomer.getFirstName()).isNotNull();
//    }

  @Test
  @Disabled
  void update() {
  }

  @Test
  @Disabled
  void delete() {
  }
}
