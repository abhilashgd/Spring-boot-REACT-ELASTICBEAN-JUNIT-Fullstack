package com.example.demo.student;

import com.example.demo.student.exception.BadRequestException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {
    @Mock
    private StudentRepository studentRepository;
    //private AutoCloseable autoCloseable;
    private StudentService underTest;

    @BeforeEach
    void setUp() {
       // autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new StudentService(studentRepository);
    }
/*
    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }
 */
    @Test
    void canGetAllStudents() {
        //given

        //when
        underTest.getAllStudents();
        //then
        verify(studentRepository).findAll();
    }

    @Test
    void canAddStudent() {
        //given
        Student student = new Student(
                "Jamila",
                "jamila@gmail.com",
                Gender.FEMALE);
        //when
        underTest.addStudent(student);
        //then
        ArgumentCaptor<Student> studentArgumentCaptor =
                ArgumentCaptor.forClass(Student.class);
        verify(studentRepository).save(studentArgumentCaptor.capture());

        Student capturedStudent = studentArgumentCaptor.getValue();
        assertThat(capturedStudent).isEqualTo(student);
    }

    @Test
    void wllThrowWhenEmailIsTak() {
        //given
        Student student = new Student(
                "Jamila",
                "jamila@gmail.com",
                Gender.FEMALE);
        given(studentRepository.selectExistsEmail(student.getEmail())).willReturn(true);
        //when and then
        assertThatThrownBy(()->underTest.addStudent(student))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Email " + student.getEmail() + " taken");
        verify(studentRepository,never()).save(any());
    }

    @Test
    @Disabled
    void deleteStudent() {
    }
}