package com.dh.catalog.client;

import lombok.Getter;
import lombok.Setter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@FeignClient(name="api-movie",url = "http://localhost:8085")
public interface MovieServiceClient {

	@GetMapping("/api/v1/movies/{genre}")
	List<MovieDto> getMovieByGenre(@PathVariable (value = "genre") String genre);


	@Getter
	@Setter
	class MovieDto{
		private Long id;

		private String name;

		private String genre;

		private String urlStream;
	}

	@Retry(name = "retryGetMovie")
	@CircuitBreaker(name = "getMovieByGenre", fallbackMethod = "getMovieByGenreFallBack")
	private void getMovieByGenre(MovieDTO entity) {

		entity.getMovieByGenre(MovieDto.DocumentRequest.builder()
				.documentType(entity.getDocumentType())
				.documentValue(entity.getDocumentNumber())
				.build());
	}

	public void getMovieByGenre(MovieDto entity, Throwable t) throws Exception {
		throw new Exception("Movies not found");
	}

	@GetMapping("/api/v1/series/{genre}")
	List<SerieDto> getSerieByGenre(@PathVariable (value = "genre") String genre);

	@Getter
	@Setter
	class SerieDto{
		private String id;
		private String name;
		private String genre;
	}

	@Retry(name = "retryGetSerie")
	@CircuitBreaker(name = "getSerieByGenre", fallbackMethod = "getSerieByGenreFallBack")
	private void getMovieByGenre(SerieDTO entity) {

		entity.getSerieByGenre(MovieDto.DocumentRequest.builder()
				.documentType(entity.getDocumentType())
				.documentValue(entity.getDocumentNumber())
				.build());
	}

	public void getSerieByGenre(MovieDto entity, Throwable t) throws Exception {
		throw new Exception("No series found");
	}

}
