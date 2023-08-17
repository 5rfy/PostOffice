package com.example.postoffice.service;

import com.example.postoffice.model.PostOffice;
import com.example.postoffice.repository.PostOfficeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

public class PostOfficeServiceTest {

    @Mock
    private PostOfficeRepository postOfficeRepository;

    @InjectMocks
    private PostOfficeService postOfficeService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    @Test
    public void ShouldFindPostOfficeWhenExist() {
        String postIndex = "123456";
        PostOffice expectedPostOffice = new PostOffice();
        when(postOfficeRepository.findPostOfficeByIndex(postIndex)).thenReturn(Optional.of(expectedPostOffice));

        Optional<PostOffice> result = postOfficeService.findPostOffice(postIndex);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(expectedPostOffice, result.get());

        verify(postOfficeRepository, times(1)).findPostOfficeByIndex(postIndex);
    }

    @Test
    public void ShouldNotFindThePostOffice() {
        String postIndex = "654321";
        when(postOfficeRepository.findPostOfficeByIndex(postIndex)).thenReturn(Optional.empty());

        Optional<PostOffice> result = postOfficeService.findPostOffice(postIndex);

        assertFalse(result.isPresent());

        verify(postOfficeRepository, times(1)).findPostOfficeByIndex(postIndex);

    }
}
