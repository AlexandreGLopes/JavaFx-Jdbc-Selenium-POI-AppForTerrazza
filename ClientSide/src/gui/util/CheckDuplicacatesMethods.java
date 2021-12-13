package gui.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.entities.Costumer;
import model.services.CostumerService;

public class CheckDuplicacatesMethods {

	public static List<Costumer> checkDuplicatesByName(CostumerService service) {

		List<Costumer> list = service.findAllofCurrentDateOrderByNameExceptCancelled();
		List<Costumer> duplicatedList = new ArrayList<>();

		// System.out.println(list.size());
		// Sempre começa com o primeir elemento em true porque sempre começa no primeiro
		boolean first = true;
		// streak é false por que só seta como true quando tiver uma repetição de iguais
		boolean streak = false;

		for (int i = 0; i + 1 < list.size(); i++) {
			Costumer costumer = list.get(i);
			Costumer costumerPos;
			Costumer costumerPre;
			// Se for o primeiro elemento
			if (first == true) {
				costumerPos = list.get(i + 1);
				// Compara com o elemento posterior
				if (costumer.getNome().equals(costumerPos.getNome())) {
					// Se for igual adiciona na outra lista
					duplicatedList.add(costumer);
					// inicio ou continuação de um streak
					streak = true;
					// Não é mais o primeiro elemento
					first = false;

				}
			} else {
				// Não é mais o primeiro elemento
				first = false;
				// Se for o último elemmento
				if (i + 1 == list.size()) {
					costumerPre = list.get(i - 1);
					// Comparada com o elemento anterior
					if (costumer.getNome().equals(costumerPre.getNome())) {
						// Se for igual adiciona na outra lista
						duplicatedList.add(costumer);
						// inicio ou continuação de um streak
						streak = true;
					}
				}
				// Se não for o último e nem o primeiro elemento
				else {
					costumerPos = list.get(i + 1);
					costumerPre = list.get(i - 1);
					// Compara com o elemento anterior e o elemento posterior
					if (costumer.getNome().equals(costumerPre.getNome())
							|| costumer.getNome().equals(costumerPos.getNome())) {
						// Se for igual adiciona na outra lista
						duplicatedList.add(costumer);
						// inicio ou continuação de um streak
						streak = true;
					}
					// Modifiquei esse código que acrescenta uma linha vazia abaixo dos clientes
					// duplicados porque ele não funcionava quando tinha uum cliente duplicado logo
					// após o outro.
					/*
					 * // Quando não tiver igualdade entre os elementos else { // Se estivássemos em
					 * um streak if (streak == true) { Costumer emptyCostumer = new Costumer();
					 * emptyCostumer.setData(new Date(0)); emptyCostumer.setHora(new Date(0));
					 * duplicatedList.add(emptyCostumer); // System.out.println(""); // final de um
					 * streak streak = false; } }
					 */
					if (streak == true && !costumer.getNome().equals(costumerPos.getNome())
							&& costumer.getNome().equals(costumerPre.getNome())) {
						Costumer emptyCostumer = new Costumer();
						emptyCostumer.setData(new Date(0));
						emptyCostumer.setHora(new Date(0));
						duplicatedList.add(emptyCostumer);
						// final de um streak
						streak = false;
					}
				}
			}
		}
		return duplicatedList;
	}

	public static List<Costumer> checkDuplicatesByTelephone(CostumerService service) {

		List<Costumer> list = service.findAllofCurrentDateOrderByTelephoneExceptCancelled();
		List<Costumer> duplicatedList = new ArrayList<>();

		// System.out.println(list.size());
		// Sempre começa com o primeir elemento em true porque sempre começa no primeiro
		boolean first = true;
		// streak é false por que só seta como true quando tiver uma repetição de iguais
		boolean streak = false;

		for (int i = 0; i + 1 < list.size(); i++) {
			Costumer costumer = list.get(i);
			Costumer costumerPos;
			Costumer costumerPre;
			// Se for o primeiro elemento
			if (first == true) {
				costumerPos = list.get(i + 1);
				// Compara com o elemento posterior
				if (costumer.getTelefone().equals(costumerPos.getTelefone())) {
					// Se for igual adiciona na outra lista
					duplicatedList.add(costumer);
					// inicio ou continuação de um streak
					streak = true;
					// Não é mais o primeiro elemento
					first = false;

				}
			} else {
				// Não é mais o primeiro elemento
				first = false;
				// Se for o último elemmento
				if (i + 1 == list.size()) {
					costumerPre = list.get(i - 1);
					// Comparada com o elemento anterior
					if (costumer.getTelefone().equals(costumerPre.getTelefone())) {
						// Se for igual adiciona na outra lista
						duplicatedList.add(costumer);
						// inicio ou continuação de um streak
						streak = true;
					}
				}
				// Se não for o último e nem o primeiro elemento
				else {
					costumerPos = list.get(i + 1);
					costumerPre = list.get(i - 1);
					// Compara com o elemento anterior e o elemento posterior
					if (costumer.getTelefone().equals(costumerPre.getTelefone())
							|| costumer.getTelefone().equals(costumerPos.getTelefone())) {
						// Se for igual adiciona na outra lista
						duplicatedList.add(costumer);
						// inicio ou continuação de um streak
						streak = true;
					}
					// Modifiquei esse código que acrescenta uma linha vazia abaixo dos clientes
					// duplicados porque ele não funcionava quando tinha uum cliente duplicado logo
					// após o outro.
					/*
					 * // Quando não tiver igualdade entre os elementos else { // Se estivássemos em
					 * um streak if (streak == true) { Costumer emptyCostumer = new Costumer();
					 * emptyCostumer.setData(new Date(0)); emptyCostumer.setHora(new Date(0));
					 * duplicatedList.add(emptyCostumer); // System.out.println(""); // final de um
					 * streak streak = false; } }
					 */
					if (streak == true && !costumer.getTelefone().equals(costumerPos.getTelefone())
							&& costumer.getTelefone().equals(costumerPre.getTelefone())) {
						Costumer emptyCostumer = new Costumer();
						emptyCostumer.setData(new Date(0));
						emptyCostumer.setHora(new Date(0));
						duplicatedList.add(emptyCostumer);
						// final de um streak
						streak = false;
					}
				}
			}
		}
		return duplicatedList;
	}
	
	public static List<Costumer> checkDuplicatesByEmail(CostumerService service) {

		List<Costumer> list = service.findAllofCurrentDateOrderByEmailExceptCancelled();
		List<Costumer> duplicatedList = new ArrayList<>();

		// System.out.println(list.size());
		// Sempre começa com o primeir elemento em true porque sempre começa no primeiro
		boolean first = true;
		// streak é false por que só seta como true quando tiver uma repetição de iguais
		boolean streak = false;

		for (int i = 0; i + 1 < list.size(); i++) {
			Costumer costumer = list.get(i);
			Costumer costumerPos;
			Costumer costumerPre;
			// Se for o primeiro elemento
			if (first == true) {
				costumerPos = list.get(i + 1);
				// Compara com o elemento posterior
				if (costumer.getEmail().equals(costumerPos.getEmail())) {
					// Se for igual adiciona na outra lista
					duplicatedList.add(costumer);
					// inicio ou continuação de um streak
					streak = true;
					// Não é mais o primeiro elemento
					first = false;

				}
			} else {
				// Não é mais o primeiro elemento
				first = false;
				// Se for o último elemmento
				if (i + 1 == list.size()) {
					costumerPre = list.get(i - 1);
					// Comparada com o elemento anterior
					if (costumer.getEmail().equals(costumerPre.getEmail())) {
						// Se for igual adiciona na outra lista
						duplicatedList.add(costumer);
						// inicio ou continuação de um streak
						streak = true;
					}
				}
				// Se não for o último e nem o primeiro elemento
				else {
					costumerPos = list.get(i + 1);
					costumerPre = list.get(i - 1);
					// Compara com o elemento anterior e o elemento posterior
					if (costumer.getEmail().equals(costumerPre.getEmail())
							|| costumer.getEmail().equals(costumerPos.getEmail())) {
						// Se for igual adiciona na outra lista
						duplicatedList.add(costumer);
						// inicio ou continuação de um streak
						streak = true;
					}
					// Modifiquei esse código que acrescenta uma linha vazia abaixo dos clientes
					// duplicados porque ele não funcionava quando tinha uum cliente duplicado logo
					// após o outro.
					/*
					 * // Quando não tiver igualdade entre os elementos else { // Se estivássemos em
					 * um streak if (streak == true) { Costumer emptyCostumer = new Costumer();
					 * emptyCostumer.setData(new Date(0)); emptyCostumer.setHora(new Date(0));
					 * duplicatedList.add(emptyCostumer); // System.out.println(""); // final de um
					 * streak streak = false; } }
					 */
					if (streak == true && !costumer.getEmail().equals(costumerPos.getEmail())
							&& costumer.getEmail().equals(costumerPre.getEmail())) {
						Costumer emptyCostumer = new Costumer();
						emptyCostumer.setData(new Date(0));
						emptyCostumer.setHora(new Date(0));
						duplicatedList.add(emptyCostumer);
						// final de um streak
						streak = false;
					}
				}
			}
		}
		return duplicatedList;
	}

}
