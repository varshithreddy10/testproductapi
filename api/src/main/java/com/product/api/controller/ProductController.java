package com.product.api.controller;

import com.product.api.dto.CategoryDto;
import com.product.api.dto.ProductDto;
import com.product.api.entity.ProductCategory;
import com.product.api.exception.ResourceNotFoundException;
import com.product.api.repository.CategoryRepository;
import com.product.api.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
public class ProductController
{
    @Autowired
    private ProductService productservice;

    @Autowired
    private CategoryRepository catrepo;

    @Autowired
    private ModelMapper modelmapper;

    @GetMapping("/get/allproducts")
    public ResponseEntity<List<ProductDto>> getAllProducts()
    {
        List<ProductDto> allproducts = productservice.getAllProducts();
        return new ResponseEntity<>(allproducts , HttpStatus.FOUND);
    }

    @GetMapping("/get/products/{categoryId}")
    public ResponseEntity<List<ProductDto>> getAllProductsByCategory(@PathVariable Long categoryId)
    {
        List<ProductDto> allproducts = productservice.getAllProductsbyCategory(categoryId);
        return new ResponseEntity<>(allproducts , HttpStatus.FOUND);
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductDto>> searchProductByName(String keyword)
    {
        List<ProductDto> allproducts = productservice.findProductByName('%' + keyword + '%');
        return new ResponseEntity<>(allproducts , HttpStatus.FOUND);
    }

    @PostMapping("/add/product/{categoryId}")
    public  ResponseEntity<ProductDto> createProduct(@RequestBody  ProductDto productdto ,@PathVariable Long categoryId)
    {
        ProductDto productdtocreated = productservice.createProduct(productdto , categoryId);
        return new ResponseEntity<>(productdtocreated , HttpStatus.FOUND);
    }

    @DeleteMapping("/delete/{productId}")
    public  ResponseEntity<String> deleteProduct(@PathVariable Long productId)
    {
        productservice.deleteProduct(productId);
        return new ResponseEntity<>("success" , HttpStatus.OK);
    }

    @PostMapping("/create/category/")
    public  ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto categorydto)
    {

        CategoryDto productdtocreated = productservice.createCategory(categorydto );
        return new ResponseEntity<>(productdtocreated , HttpStatus.FOUND);
    }

    @GetMapping("/get/all/categories")
    public ResponseEntity<List<CategoryDto>> getAllCategories()
    {

        List<ProductCategory> allcategoryentity = catrepo.findAll();
        List<CategoryDto> allcategorydtos=allcategoryentity.stream()
                .map(individualCategoryentity -> modelmapper.map(individualCategoryentity , CategoryDto.class))
                .toList();

        return new ResponseEntity<>(allcategorydtos , HttpStatus.FOUND);
    }

    @GetMapping("/get/category/byid/{categoryId}")
    public ResponseEntity<CategoryDto> getCategorybyId(@PathVariable Long categoryId)
    {
        ProductCategory categoryentity = catrepo.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("category","categoryId",""+categoryId));

        CategoryDto categorydto = modelmapper.map(categoryentity , CategoryDto.class);

        return new ResponseEntity<>(categorydto , HttpStatus.FOUND);
    }

    @GetMapping("/get/product/{productId}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable Long productId)
    {
        ProductDto productdto = productservice.getProductByproductId(productId);
        return new ResponseEntity<>(productdto , HttpStatus.FOUND);
    }
}
