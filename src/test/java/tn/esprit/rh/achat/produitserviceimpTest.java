package tn.esprit.rh.achat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.rh.achat.entities.CategorieProduit;
import tn.esprit.rh.achat.entities.Produit;
import tn.esprit.rh.achat.entities.Stock;
import tn.esprit.rh.achat.repositories.ProduitRepository;
import tn.esprit.rh.achat.repositories.StockRepository;
import tn.esprit.rh.achat.services.ProduitServiceImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;




@ExtendWith(MockitoExtension.class)
class produitserviceimpTest  {
    @Mock
    ProduitRepository produitRepository;

    @Mock
    StockRepository stockRepository;


    @InjectMocks
    ProduitServiceImpl produitService;

    private Produit produit;

    @BeforeEach
    void setUp() {
        produit = new Produit();
        produit.setIdProduit(1L);
        produit.setCodeProduit("testprod");
        produit.setLibelleProduit("test Product");
        produit.setPrix(50.0f);

        // Initialize date properties
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            produit.setDateCreation(dateFormat.parse("2023-10-24"));
            produit.setDateDerniereModification(dateFormat.parse("2023-10-24"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Stock stock = new Stock();
        stock.setIdStock(2L);
        stock.setLibelleStock("test Stock");
        stock.setQte(100);
        stock.setQteMin(10);

        CategorieProduit categorieProduit = new CategorieProduit();
        categorieProduit.setIdCategorieProduit(3L);
        categorieProduit.setCodeCategorie("testcat");
        categorieProduit.setLibelleCategorie("test Category");

        produit.setStock(stock);
        produit.setCategorieProduit(categorieProduit);
    }



    @Test
    void retrieveAllProduitsTest() {
        // Create a list using ArrayList
        List<Produit> produitsList = new ArrayList<>();
        produitsList.add(produit);

        // Mock the behavior of produitRepository.findAll()
        when(produitRepository.findAll()).thenReturn(produitsList);

        // Call the method under test
        List<Produit> retrievedProduitsList = produitService.retrieveAllProduits();

        // Use assertThat(actual).hasSize(expected)
        assertThat(retrievedProduitsList).hasSize(1);
        verify(produitRepository).findAll();
    }

    @Test
    void addProduitTest() {
        // Mock the behavior of produitRepository.save()
        when(produitRepository.save(Mockito.any(Produit.class))).thenReturn(produit);

        // Call the method under test
        Produit savedProduit = produitService.addProduit(produit);

        // Assertions
        assertThat(savedProduit).isNotNull();
        verify(produitRepository).save(Mockito.any(Produit.class));
    }

    @Test
    void deleteProduitTest() {
        Long produitId = 1L;
        // Mock the behavior of produitRepository.deleteById()
        doNothing().when(produitRepository).deleteById(produitId);

        // Call the method under test
        produitService.deleteProduit(produitId);

        // Verify that deleteById was called once
        verify(produitRepository, times(1)).deleteById(produitId);
    }

    @Test
    void updateProduitTest() {
        // Mock the behavior of produitRepository.save()
        when(produitRepository.save(produit)).thenReturn(produit);

        // Modify some properties of the produit
        produit.setLibelleProduit("Updated Product Name");

        // Call the method under test
        Produit updatedProduit = produitService.updateProduit(produit);

        // Assertions
        assertThat(updatedProduit).isNotNull();
        assertThat(updatedProduit.getLibelleProduit()).isEqualTo("Updated Product Name");
        verify(produitRepository).save(produit);
    }

    @Test
    void retrieveProduitTest() {
        Long produitId = 1L;
        // Mock the behavior of produitRepository.findById()
        when(produitRepository.findById(produitId)).thenReturn(Optional.of(produit));

        // Call the method under test
        Produit retrievedProduit = produitService.retrieveProduit(produitId);

        // Assertions
        assertThat(retrievedProduit).isNotNull();
        verify(produitRepository).findById(produitId);
    }

    @Test
    void assignProduitToStockTest() {
        Long idProduit = 1L;
        Long idStock = 2L;

        // Mock the behavior of produitRepository.findById() and stockRepository.findById()
        when(produitRepository.findById(idProduit)).thenReturn(Optional.of(produit));
        when(stockRepository.findById(idStock)).thenReturn(Optional.of(new Stock()));

        // Call the method under test
        produitService.assignProduitToStock(idProduit, idStock);

        // Assertions or verifications as needed
        verify(produitRepository).save(produit);
    }


}