package us.myles_selim.alchemical_brews;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import scala.actors.threadpool.Arrays;
import us.myles_selim.alchemical_brews.ingredients.IngredientStack;

public class IngredientList implements List<IngredientStack> {

	private List<IngredientStack> ingredients;

	public IngredientList() {
		this.ingredients = new LinkedList<>();
	}

	public IngredientList(List<IngredientStack> ingredients) {
		if (ingredients == null)
			this.ingredients = new LinkedList<>();
		else
			this.ingredients = new LinkedList<>(ingredients);
	}

	public static IngredientList unmodifiableList(IngredientList ingredients) {
		IngredientList list = new IngredientList();
		list.ingredients = Collections.unmodifiableList(ingredients);
		return list;
	}

	@SuppressWarnings("unchecked")
	public static IngredientList asList(IngredientStack[] ingredients) {
		IngredientList list = new IngredientList();
		list.ingredients = Arrays.asList(ingredients);
		return list;
	}

	@Override
	public boolean add(IngredientStack arg0) {
		return this.ingredients.add(arg0);
	}

	@Override
	public void add(int arg0, IngredientStack arg1) {
		this.ingredients.add(arg0, arg1);
	}

	@Override
	public boolean addAll(Collection<? extends IngredientStack> arg0) {
		return this.ingredients.addAll(arg0);
	}

	@Override
	public boolean addAll(int arg0, Collection<? extends IngredientStack> arg1) {
		return this.ingredients.addAll(arg0, arg1);
	}

	@Override
	public void clear() {
		this.ingredients.clear();
	}

	public boolean containsPrecise(Object arg0) {
		for (IngredientStack stack : this.ingredients)
			if (stack.equalsPrecise(arg0))
				return true;
		return false;
	}

	@Override
	public boolean contains(Object arg0) {
		return this.ingredients.contains(arg0);
	}

	public boolean containsAllPrecise(Collection<?> arg0) {
		for (Object v : arg0)
			if (!containsPrecise(v))
				return false;
		return true;
	}

	@Override
	public boolean containsAll(Collection<?> arg0) {
		return this.ingredients.containsAll(arg0);
	}

	@Override
	public IngredientStack get(int arg0) {
		return this.ingredients.get(arg0);
	}

	public int indexOfPrecise(Object arg0) {
		for (int i = 0; i < this.ingredients.size(); i++)
			if (this.ingredients.get(i).equalsPrecise(arg0))
				return i;
		return -1;
	}

	@Override
	public int indexOf(Object arg0) {
		return this.ingredients.indexOf(arg0);
	}

	@Override
	public boolean isEmpty() {
		return this.ingredients.isEmpty();
	}

	@Override
	public Iterator<IngredientStack> iterator() {
		return this.ingredients.iterator();
	}

	@Override
	public int lastIndexOf(Object arg0) {
		return this.ingredients.lastIndexOf(arg0);
	}

	@Override
	public ListIterator<IngredientStack> listIterator() {
		return this.ingredients.listIterator();
	}

	@Override
	public ListIterator<IngredientStack> listIterator(int arg0) {
		return this.ingredients.listIterator(arg0);
	}

	public boolean removePrecise(Object arg0) {
		for (int i = 0; i < this.ingredients.size(); i++) {
			if (this.ingredients.get(i).equalsPrecise(arg0)) {
				remove(i);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean remove(Object arg0) {
		return this.ingredients.remove(arg0);
	}

	@Override
	public IngredientStack remove(int arg0) {
		return this.ingredients.remove(arg0);
	}

	public boolean removeAllPrecise(Collection<?> arg0) {
		boolean changed = false;
		for (Object o : arg0) {
			if (removePrecise(o))
				changed = true;
		}
		return changed;
	}

	@Override
	public boolean removeAll(Collection<?> arg0) {
		return this.ingredients.removeAll(arg0);
	}

	// TODO: implement percise version
	@Override
	public boolean retainAll(Collection<?> arg0) {
		throw new UnsupportedOperationException();
		// return this.ingredients.retainAll(arg0);
	}

	@Override
	public IngredientStack set(int arg0, IngredientStack arg1) {
		return this.ingredients.set(arg0, arg1);
	}

	@Override
	public int size() {
		return this.ingredients.size();
	}

	@Override
	public List<IngredientStack> subList(int arg0, int arg1) {
		return this.ingredients.subList(arg0, arg1);
	}

	@Override
	public Object[] toArray() {
		return this.ingredients.toArray();
	}

	@Override
	public <T> T[] toArray(T[] arg0) {
		return this.ingredients.toArray(arg0);
	}

}
