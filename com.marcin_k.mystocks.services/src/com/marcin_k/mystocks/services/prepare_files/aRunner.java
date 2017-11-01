package com.marcin_k.mystocks.services.prepare_files;

import java.io.IOException;

public class aRunner {

	public static void main(String[] args) throws IOException {
		System.out.println("MSG from aRunner Class test is running");
		FilesController.getInstance().getAllFiles();
	}

}
