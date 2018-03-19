package com.zandero.rest.data;

import com.zandero.rest.reader.ValueReader;
import com.zandero.utils.Assert;
import com.zandero.utils.StringUtils;
import com.zandero.utils.extra.ValidatingUtils;

/**
 *
 */
public class MethodParameter {

	/**
	 * Query or Path type
	 */
	private ParameterType type;

	/**
	 * parameter to search for in method annotations {@code @PathParam} {@code @QueryParam}
	 */
	private String name;

	/**
	 * index matching method argument index 0..N-1
	 */
	private int index = -1;

	/**
	 * Absolute index in path (if path parameter)
	 */
	private int pathIndex = -1;

	/**
	 * Index of argument in reg ex if not given as @PathParam()
	 */
	private int regExIndex = -1;

	/**
	 * type of parameter expected by method
	 */
	private Class<?> dataType;

	/**
	 * default value of parameter in case not given on call
	 */
	private String defaultValue;

	/**
	 * path is a regular expression
	 */
	private String regularExpression;

	/**
	 * String to type converter
	 */
	private Class<? extends ValueReader> reader;


	public MethodParameter(ParameterType parameterType, String paramName) {

		Assert.notNull(parameterType, "Missing parameter type!");
		Assert.notNullOrEmptyTrimmed(paramName, "Missing parameter name!");

		type = parameterType;
		name = StringUtils.trim(paramName);
	}

	public MethodParameter(ParameterType parameterType, String paramName, Class<?> argumentType, int argumentIndex) {

		this(parameterType, paramName);

		Assert.isTrue(argumentIndex >= 0, "Can't set negative argument index!");
		argument(argumentType, argumentIndex);
	}

	public MethodParameter argument(Class<?> argumentType, int argumentIndex) {

		Assert.isTrue(argumentIndex >= 0, "Can't set negative argument index!");
		Assert.notNull(argumentType, "Missing argument type!");

		dataType = argumentType;
		index = argumentIndex;

		return this;
	}

	public MethodParameter join(MethodParameter joining) {

		if (ParameterType.body.equals(type) &&
		    !ParameterType.body.equals(joining.type)) {
			setType(joining.getType());
			setName(joining.name);
		}

		if (reader == null) {
			reader = joining.reader;
		}

		if (index == -1) {
			index = joining.index;
		}

		if (pathIndex == -1) {
			pathIndex = joining.pathIndex;
		}

		if (regExIndex == -1) {
			regExIndex = joining.regExIndex;
		}

		if (regularExpression == null) {
			regularExpression = joining.regularExpression;
		}

		if (defaultValue == null) {
			defaultValue = joining.defaultValue;
		}

		return this;
	}

	public ParameterType getType() {
		return type;
	}

	public void setType(ParameterType value) {
		type = value;
	}

	public String getName() {

		return name;
	}

	public void setName(String value) {
		name = value;
	}

	public int getIndex() {
		return index;
	}

	public int getRegExIndex() {
		return regExIndex;
	}

	public Class<?> getDataType() {
		return dataType;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String value) {
		defaultValue = StringUtils.trimToNull(value);
	}

	public String getRegEx() {
		return regularExpression;
	}

	public void setRegEx(String value, int index) {
		value = StringUtils.trimToNull(value);

		if (value != null) {
			Assert.isTrue(ValidatingUtils.isRegEx(value), "Invalid regular expression: '" + value + "'!");
		}

		Assert.isTrue(index >= 0, "Can't set negative regular expression index!");

		regularExpression = value;
		regExIndex = index;
	}

	public boolean isRegEx() {
		return regularExpression != null;
	}

	public void setPathIndex(int value) {
		pathIndex = value;
	}

	public int getPathIndex() {
		return pathIndex;
	}

	public void setValueReader(Class<? extends ValueReader> valueReader) {
		reader = valueReader;
	}

	public Class<? extends ValueReader> getReader() {
		return reader;
	}

	public boolean isBody() {
		return ParameterType.body.equals(type);
	}

	public boolean isUsedAsArgument() {
		return index >= 0;
	}

	@Override
	public String toString() {
		if (ParameterType.body.equals(type)) {
			return type.getDescription();
		}

		return type.getDescription() + "(\"" + name + "\")";
	}
}
